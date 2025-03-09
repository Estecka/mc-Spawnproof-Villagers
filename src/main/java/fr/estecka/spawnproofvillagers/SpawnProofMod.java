package fr.estecka.spawnproofvillagers;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.gamerule.v1.CustomGameRuleCategory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.GameRules;
import net.minecraft.world.GameRules.IntRule;
import net.minecraft.world.GameRules.Key;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory.createIntRule;


public class SpawnProofMod
implements ModInitializer
{
	static public final String MODID = "spawnproof-villagers";
	static public final Logger LOGGER = LoggerFactory.getLogger(MODID);

	static public final CustomGameRuleCategory CATEGORY = new CustomGameRuleCategory(Identifier.of(MODID, "gamerules"), Text.translatable("gamerule.category."+MODID).formatted(Formatting.YELLOW, Formatting.BOLD));
	static public final Key<IntRule> RANGE_RULE = Register("spawnproofing.range", createIntRule(24, 0, 128));

	@Override
	public void onInitialize() {
		// Static init;
	}

	static private <T extends GameRules.Rule<T>> GameRules.Key<T>	Register(String name, GameRules.Type<T> type){
		return GameRuleRegistry.register(MODID+"."+name, CATEGORY, type);
	}
}
