package raftgame.data;

/**
 * Represents a map tile object.
 * Stores data if map is water or raft.
 */
public class Tile {
    private boolean isRaft = false;
    private boolean buildRaftHere = false;

    public boolean isRaft() {
        return isRaft;
    }

    public void setRaft(boolean raft) {
        isRaft = raft;
    }

    public boolean isBuildRaftHere() {
        return buildRaftHere;
    }

    public void setBuildRaftHere(boolean buildRaftHere) {
        this.buildRaftHere = buildRaftHere;
    }
}
