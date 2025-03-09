package fr.estecka.spawnproofvillagers.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import fr.estecka.spawnproofvillagers.SpawnProofMod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.SpawnHelper;

@Mixin(SpawnHelper.class)
public class SpawnHelperMixin
{
	@WrapOperation(
		method="spawnEntitiesInChunk (Lnet/minecraft/entity/SpawnGroup;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/chunk/Chunk;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/SpawnHelper$Checker;Lnet/minecraft/world/SpawnHelper$Runner;)V", 
		at=@At(value="INVOKE", target="net/minecraft/server/world/ServerWorld.getClosestPlayer (DDDDZ)Lnet/minecraft/entity/player/PlayerEntity;"),
		expect=1
	)
	static private PlayerEntity	getClosestVillager(ServerWorld world, double x, double y, double z, double maxDistance, boolean ignoreCreative, Operation<PlayerEntity> original, @Share("closest") LocalBooleanRef villagerInRange){
		int rangeMax = world.getServer().getGameRules().getInt(SpawnProofMod.RANGE_RULE);
		double squaredMax = rangeMax*rangeMax;


		for(Entity entity : world.iterateEntities())
		if (entity instanceof VillagerEntity &&  entity.squaredDistanceTo(x,y,z) <= squaredMax)
		{
			villagerInRange.set(true);
			break;
		}

		return original.call(world, x, y, z, maxDistance, ignoreCreative);
	}

	@ModifyExpressionValue(
		method="spawnEntitiesInChunk (Lnet/minecraft/entity/SpawnGroup;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/chunk/Chunk;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/SpawnHelper$Checker;Lnet/minecraft/world/SpawnHelper$Runner;)V",
		at=@At(value="INVOKE", target="net/minecraft/entity/player/PlayerEntity.squaredDistanceTo (DDD)D"),
		expect=1
	)
	static private double	getClosestDistance(double original, @Share("closest") LocalBooleanRef isvillagerInRange){
		if (isvillagerInRange.get())
			return 0;
		else
			return original;
	}
}
