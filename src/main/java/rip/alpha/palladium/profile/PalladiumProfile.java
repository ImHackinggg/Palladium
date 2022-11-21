package rip.alpha.palladium.profile;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Data
@RequiredArgsConstructor
public class PalladiumProfile {

    private final UUID uuid;
    private int packets = 0, leftClicks = 0, rightClicks = 0, packetVls = 0;
    private boolean backpacking = false;

    public int incrementVl() {
        this.packets = 0;
        return this.packetVls = this.packetVls + 1;
    }

    public int incrementPackets() {
        return this.packets = this.packets + 1;
    }

    public void incrementLeftClick() {
        this.leftClicks = this.leftClicks + 1;
    }

    public void incrementRightClick() {
        this.rightClicks = this.rightClicks + 1;
    }

    public void clear() {
        this.packets = 0;
        this.rightClicks = 0;
        this.leftClicks = 0;
    }
}
