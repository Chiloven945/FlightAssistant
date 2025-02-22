package ru.octol1ttle.flightassistant.alerts.impl.fault;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import ru.octol1ttle.flightassistant.DrawHelper;
import ru.octol1ttle.flightassistant.alerts.api.BaseAlert;
import ru.octol1ttle.flightassistant.alerts.api.IECAMAlert;
import ru.octol1ttle.flightassistant.alerts.impl.AlertSoundData;
import ru.octol1ttle.flightassistant.computers.api.IComputer;
import ru.octol1ttle.flightassistant.computers.api.ITickableComputer;
import ru.octol1ttle.flightassistant.config.FAConfig;
import ru.octol1ttle.flightassistant.registries.ComputerRegistry;

public class ComputerFaultAlert extends BaseAlert implements IECAMAlert {
    @Override
    public boolean isTriggered() {
        return ComputerRegistry.anyFaulted();
    }

    @Override
    public @NotNull AlertSoundData getSoundData() {
        return AlertSoundData.MASTER_CAUTION;
    }

    @Override
    public int render(TextRenderer textRenderer, DrawContext context, int x, int y, boolean highlight) {
        int i = 0;

        for (IComputer computer : ComputerRegistry.getComputers()) {
            if (computer instanceof ITickableComputer tickable && ComputerRegistry.isFaulted(computer.getClass())) {
                for (int j = 0; j < Integer.MAX_VALUE; j++) {
                    String key = "%s.%d".formatted(tickable.getFaultTextBaseKey(), j);
                    if (!I18n.hasTranslation(key)) {
                        break;
                    }
                    if (j == 0) {
                        i += DrawHelper.drawHighlightedText(textRenderer, context, Text.translatable(key), x, y, FAConfig.indicator().cautionColor, highlight);
                    } else {
                        i += DrawHelper.drawHighlightedText(textRenderer, context, Text.translatable(key), x, y, FAConfig.indicator().advisoryColor, false);
                    }

                    y += 10;
                }
            }
        }

        return i;
    }
}
