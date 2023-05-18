public class PlayerStatus {
    private final boolean canMoveVertically;
    private final boolean canMoveHorizontally;
    private final int damage;
    private final boolean isSlowed;

    public PlayerStatus(boolean canMoveVertically, boolean canMoveHorizontally, int damage, boolean isSlowed) {
        this.canMoveVertically = canMoveVertically;
        this.canMoveHorizontally = canMoveHorizontally;
        this.damage = damage;
        this.isSlowed = isSlowed;
    }

    public boolean canMoveVertically() {
        return canMoveVertically;
    }

    public boolean canMoveHorizontally() {
        return canMoveHorizontally;
    }

    public int getDamage() {
        return damage;
    }

    public boolean isSlowed() {
        return isSlowed;
    }
}
