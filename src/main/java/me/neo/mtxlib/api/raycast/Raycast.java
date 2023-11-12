package me.neo.mtxlib.api.raycast;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collection;

@SuppressWarnings("all")
public class Raycast {
    private final double DIVIDER = 100.0D;
    private ArrayList<Material> whitelistedMats = new ArrayList<>();
    private ArrayList<Location> points = new ArrayList<>();

    private ArrayList<Block> hitBlocks = new ArrayList<>();
    private ArrayList<Location> hitBlockLocs = new ArrayList<>();
    private ArrayList<Location> hitEntLocs = new ArrayList<>();
    private ArrayList<Entity> hitEntities = new ArrayList<>();
    private World world;
    private double x, y, z, yaw, pitch, size;
    private LivingEntity owner;
    private Location currentLoc;

    public Raycast(Location loc, double size) {
        this(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch(), size);
    }

    public Raycast(World world, double x, double y, double z, double yaw, double pitch, double size) {
        whitelistedMats.add(Material.AIR);
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.size = size;
    }

    private void step(Vector rcPos) {
        rcPos = RaycastMath.rotate(rcPos, yaw, pitch);
        currentLoc = (new Location(world, x, y, z)).clone().add(rcPos.getX() / 100.0D, rcPos.getY() / 100.0D, rcPos.getZ() / 100.0D);
    }



    public boolean cast() {
        points.clear();
        hitBlocks.clear();
        hitBlockLocs.clear();
        hitEntLocs.clear();
        hitEntities.clear();
        int length = 0;
        step(new Vector(0.0D, 0.0D, length + 50.0D));
        Collection<Entity> entities = world.getNearbyEntities(currentLoc, 0.01D, 0.01D, 0.01D);
        if (!entities.isEmpty()) {
            hitEntities.addAll(entities);
            hitEntLocs.add(currentLoc);
        }

        if (!canPass(currentLoc)) {
            hitBlocks.add(currentLoc.getBlock());
            hitBlockLocs.add(currentLoc);
        }

        while (length <= size * 100.0D) {
            entities.clear();
            points.add(currentLoc);
            length++;
            step(new Vector(0, 0, length + 50.0D));

            entities = world.getNearbyEntities(currentLoc, 0.01D, 0.01D, 0.01D);
            if (!entities.isEmpty()) {
                hitEntities.addAll(entities);
                hitEntLocs.add(currentLoc);
            }

            if (!canPass(currentLoc)) {
                hitBlocks.add(currentLoc.getBlock());
                hitBlockLocs.add(currentLoc);
            }
        }
        return hitBlocks.size() > 0 || hitEntities.size() > 0;
    }
    private boolean canPass(Location loc) {
        return whitelistedMats.contains(loc.getBlock().getType());
    }

    /**
     * Traverses through all the points on the raycast and draws a particle there.
     * @param particle The type of particle to spawn.
     * @param count How many particles to spawn at each point.
     * @param xOffset The x offset of the particle.
     * @param yOffset The y offset of the particle.
     * @param zOffset The z offset of the particle.
     */
    public void draw(Particle particle, int count, double xOffset, double yOffset, double zOffset) {
        for (Location loc : points) {
            loc.getWorld().spawnParticle(particle, loc, count, xOffset, yOffset, zOffset);
        }
    }

    /**
     * Traverses through all the points on the raycast and draws a particle there.
     * @param particle The type of particle to spawn.
     * @param count How many particles to spawn at each point.
     * @param xOffset The x offset of the particle.
     * @param yOffset The y offset of the particle.
     * @param zOffset The z offset of the particle.
     * @param extra The extra data for the particle.
     */
    public void drawExtra(Particle particle, int count, double xOffset, double yOffset, double zOffset, double extra) {
        for (Location loc : points) {
            loc.getWorld().spawnParticle(particle, loc, count, xOffset, yOffset, zOffset, extra);
        }
    }

    /**
     * Traverses through all the points on the raycast and draws a particle there.
     * These particles are redstone dust particles than can be colored.
     * @param count How many particles to spawn at each point.
     * @param xOffset The x offset of the particle.
     * @param yOffset The y offset of the particle.
     * @param zOffset The z offset of the particle.
     * @param extra The extra data for the particle.
     * @param dustOptions The {@link org.bukkit.Particle.DustOptions} for the particle.
     */
    public void drawColored(int count, double xOffset, double yOffset, double zOffset, double extra, Particle.DustOptions dustOptions) {
        for (Location loc : points) {
            loc.getWorld().spawnParticle(Particle.REDSTONE, loc, count, xOffset, yOffset, zOffset, extra, dustOptions);
        }
    }


    public ArrayList<Material> getWhitelistedMats() {
        return whitelistedMats;
    }

    public Raycast setWhitelistedMats(ArrayList<Material> whitelistedMats) {
        this.whitelistedMats = whitelistedMats;
        return this;
    }

    public ArrayList<Location> getPoints() {
        return points;
    }

    public ArrayList<Block> getHitBlocks() {
        return hitBlocks;
    }

    public ArrayList<Location> getHitBlockLocs() {
        return hitBlockLocs;
    }

    public ArrayList<Location> getHitEntityLocs() {
        return hitEntLocs;
    }

    public ArrayList<Entity> getHitEntities() {
        return hitEntities;
    }

    public World getWorld() {
        return world;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public double getYaw() {
        return yaw;
    }

    public double getPitch() {
        return pitch;
    }

    public double getSize() {
        return size;
    }

    public LivingEntity getOwner() {
        return owner;
    }

    public Raycast setOwner(LivingEntity owner) {
        this.owner = owner;
        return this;
    }

    public Location getCurrentLocation() {
        return currentLoc;
    }
}
