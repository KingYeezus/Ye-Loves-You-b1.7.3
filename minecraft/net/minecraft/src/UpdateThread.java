package net.minecraft.src;

import java.util.LinkedList;
import java.util.List;
import org.lwjgl.opengl.Pbuffer;

public class UpdateThread extends Thread {
    private Pbuffer pbuffer = null;
    private Object lock = new Object();
    private List updateList = new LinkedList();
    private List updatedList = new LinkedList();
    private int updateCount = 0;
    private Tessellator mainTessellator;
    private Tessellator threadTessellator;
    private boolean working;
    private WorldRenderer currentRenderer;
    private boolean canWork;
    private boolean canWorkToEndOfUpdate;
    private static final int MAX_UPDATE_CAPACITY = 10;

    public UpdateThread(Pbuffer pbuffer) {
        super("UpdateThread");
        this.mainTessellator = Tessellator.instance;
        this.threadTessellator = new Tessellator(262144);
        this.working = false;
        this.currentRenderer = null;
        this.canWork = false;
        this.canWorkToEndOfUpdate = false;
        this.pbuffer = pbuffer;
    }

    public void addRendererToUpdate(WorldRenderer wr, boolean first) {
        synchronized(this.lock) {
            if (wr.isUpdating) {
                throw new IllegalArgumentException("Renderer already updating");
            } else {
                if (first) {
                    this.updateList.add(0, wr);
                } else {
                    this.updateList.add(wr);
                }

                wr.isUpdating = true;
                this.lock.notifyAll();
            }
        }
    }

    private WorldRenderer getRendererToUpdate() {
        synchronized(this.lock) {
            while(this.updateList.size() <= 0) {
                try {
                    this.lock.wait();
                } catch (InterruptedException var4) {
                }
            }

            this.currentRenderer = (WorldRenderer)this.updateList.remove(0);
            this.lock.notifyAll();
            return this.currentRenderer;
        }
    }

    public boolean hasWorkToDo() {
        synchronized(this.lock) {
            if (this.updateList.size() > 0) {
                return true;
            } else {
                return this.currentRenderer != null ? true : this.working;
            }
        }
    }

    public int getUpdateCapacity() {
        synchronized(this.lock) {
            return this.updateList.size() > 10 ? 0 : 10 - this.updateList.size();
        }
    }

    private void rendererUpdated(WorldRenderer wr) {
        synchronized(this.lock) {
            this.updatedList.add(wr);
            ++this.updateCount;
            this.currentRenderer = null;
            this.working = false;
            this.lock.notifyAll();
        }
    }

    private void finishUpdatedRenderers() {
        synchronized(this.lock) {
            for(int i = 0; i < this.updatedList.size(); ++i) {
                WorldRenderer wr = (WorldRenderer)this.updatedList.get(i);
                wr.finishUpdate();
                wr.isUpdating = false;
            }

            this.updatedList.clear();
        }
    }

    public void run() {
        try {
            this.pbuffer.makeCurrent();
        } catch (Exception var8) {
            var8.printStackTrace();
        }

        IUpdateListener updateListener = new IUpdateListener() {
            public void updating() {
                Tessellator.instance = UpdateThread.this.mainTessellator;
                UpdateThread.this.checkCanWork();
                Tessellator.instance = UpdateThread.this.threadTessellator;
            }
        };

        while(!Thread.interrupted()) {
            try {
                WorldRenderer wr = this.getRendererToUpdate();
                this.checkCanWork();

                try {
                    Tessellator.instance = this.threadTessellator;
                    wr.updateRenderer(updateListener);
                } finally {
                    Tessellator.instance = this.mainTessellator;
                }

                this.rendererUpdated(wr);
            } catch (Exception var9) {
                var9.printStackTrace();
                if (this.currentRenderer != null) {
                    this.currentRenderer.isUpdating = false;
                    this.currentRenderer.needsUpdate = true;
                }

                this.currentRenderer = null;
                this.working = false;
            }
        }

    }

    public void pause() {
        synchronized(this.lock) {
            this.canWork = false;
            this.canWorkToEndOfUpdate = false;
            this.lock.notifyAll();

            while(this.working) {
                try {
                    this.lock.wait();
                } catch (InterruptedException var4) {
                }
            }

            this.finishUpdatedRenderers();
        }
    }

    public void unpause() {
        synchronized(this.lock) {
            if (this.working) {
                Config.dbg("UpdateThread still working in unpause()!!!");
            }

            this.canWork = true;
            this.canWorkToEndOfUpdate = false;
            this.lock.notifyAll();
        }
    }

    public void unpauseToEndOfUpdate() {
        synchronized(this.lock) {
            if (this.working) {
                Config.dbg("UpdateThread still working in unpause()!!!");
            }

            while(this.currentRenderer != null) {
                this.canWork = false;
                this.canWorkToEndOfUpdate = true;
                this.lock.notifyAll();

                try {
                    this.lock.wait();
                } catch (InterruptedException var4) {
                }
            }

            this.pause();
        }
    }

    private void checkCanWork() {
        Thread.yield();
        synchronized(this.lock) {
            while(!this.canWork && (!this.canWorkToEndOfUpdate || this.currentRenderer == null)) {
                this.working = false;
                this.lock.notifyAll();

                try {
                    this.lock.wait();
                } catch (InterruptedException var4) {
                }
            }

            this.working = true;
            this.lock.notifyAll();
        }
    }

    public void clearAllUpdates() {
        synchronized(this.lock) {
            this.unpauseToEndOfUpdate();
            this.updateList.clear();
            this.lock.notifyAll();
        }
    }

    public int getPendingUpdatesCount() {
        synchronized(this.lock) {
            int count = this.updateList.size();
            if (this.currentRenderer != null) {
                ++count;
            }

            return count;
        }
    }

    public int resetUpdateCount() {
        synchronized(this.lock) {
            int count = this.updateCount;
            this.updateCount = 0;
            return count;
        }
    }
}
