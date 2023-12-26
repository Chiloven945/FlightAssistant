package ru.octol1ttle.flightassistant.commands.plan;

import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.Text;
import ru.octol1ttle.flightassistant.HudRenderer;
import ru.octol1ttle.flightassistant.computers.ComputerHost;

public class ExecutePlanCommand {
    public static int execute(CommandContext<FabricClientCommandSource> context, int fromWaypoint) {
        ComputerHost host = HudRenderer.getHost();
        if (host != null) {
            host.plan.execute(fromWaypoint);
            context.getSource().sendFeedback(Text.translatable("commands.flightassistant.flight_plan_executed", fromWaypoint, host.plan.size()));
        }
        return 0;
    }
}