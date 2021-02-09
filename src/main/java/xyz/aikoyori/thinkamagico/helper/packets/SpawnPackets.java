package xyz.aikoyori.thinkamagico.helper.packets;

import net.minecraft.client.network.play.IClientPlayNetHandler;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.IOException;
import java.util.function.Supplier;

public class SpawnPackets implements IPacket<IClientPlayNetHandler> {
    private final ResourceLocation id;
    private final RegistryKey<World> type;
    private final BlockPos pos;

    public SpawnPackets(PacketBuffer buf) {
        id = buf.readResourceLocation();
        type = RegistryKey.getOrCreateKey(Registry.WORLD_KEY,buf.readResourceLocation());
        pos = buf.readBlockPos();
    }

    public SpawnPackets(ResourceLocation id, RegistryKey<World> type, BlockPos pos) {
        this.id = id;
        this.type = type;
        this.pos = pos;
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeResourceLocation(id);
        buf.writeResourceLocation(type.getLocation());
        buf.writeBlockPos(pos);
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerWorld spawnWorld = ctx.get().getSender().world.getServer().getWorld(type);
            EntityType<?> entityType = ForgeRegistries.ENTITIES.getValue(id);
            if (entityType == null) {
                throw new IllegalStateException("This cannot happen! Unknown id '" + id.toString() + "'!");
            }
            entityType.spawn(spawnWorld, null, null, pos, SpawnReason.SPAWN_EGG, true, true);
        });
        return true;
    }

    @Override
    public void readPacketData(PacketBuffer buf) throws IOException {

    }

    @Override
    public void writePacketData(PacketBuffer buf) throws IOException {

    }

    @Override
    public void processPacket(IClientPlayNetHandler handler) {

    }
    @OnlyIn(Dist.CLIENT)
    public ResourceLocation getEntityID() {
        return this.id;
    }

    @OnlyIn(Dist.CLIENT)
    public double getX() {
        return this.pos.getX();
    }

    @OnlyIn(Dist.CLIENT)
    public double getY() {
        return this.pos.getY();
    }

    @OnlyIn(Dist.CLIENT)
    public double getZ() {
        return this.pos.getZ();
    }

}
