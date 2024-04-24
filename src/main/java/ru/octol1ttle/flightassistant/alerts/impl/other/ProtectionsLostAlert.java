package ru.octol1ttle.flightassistant.alerts.impl.other;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import ru.octol1ttle.flightassistant.DrawHelper;
import ru.octol1ttle.flightassistant.alerts.api.BaseAlert;
import ru.octol1ttle.flightassistant.alerts.api.IECAMAlert;
import ru.octol1ttle.flightassistant.alerts.impl.AlertSoundData;
import ru.octol1ttle.flightassistant.computers.impl.safety.FlightProtectionsComputer;
import ru.octol1ttle.flightassistant.config.FAConfig;
import ru.octol1ttle.flightassistant.registries.ComputerRegistry;

public class ProtectionsLostAlert extends BaseAlert implements IECAMAlert {
    private final FlightProtectionsComputer prot = ComputerRegistry.resolve(FlightProtectionsComputer.class);

    @Override
    public boolean isTriggered() {
        return prot.law != FlightProtectionsComputer.FlightControlLaw.NORMAL;
    }

    @Override
    public @NotNull AlertSoundData getSoundData() {
        return AlertSoundData.MASTER_WARNING;
    }

    @Override
    public int render(TextRenderer textRenderer, DrawContext context, int x, int y, boolean highlight) {
        return DrawHelper.drawHighlightedText(textRenderer, context,
                Text.translatable("alerts.flightassistant.fault.computers.flight_prot"), x, y,
                FAConfig.indicator().warningColor, highlight
        );
    }
}