package rip.alpha.palladium.profile;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PalladiumProfileTask implements Runnable {

    private final PalladiumProfileHandler profileHandler;
    private int seconds = 0;

    @Override
    public void run() {
        for (PalladiumProfile profile : this.profileHandler.getProfiles()) {
            try {
                profile.clear();

                if (profile.getPacketVls() > 0) {
                    if (seconds >= 15) {
                        profile.setPacketVls(Math.max(0, profile.getPacketVls() - 1));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (seconds >= 15) {
            this.seconds = 0;
        }
        this.seconds++;
    }
}
