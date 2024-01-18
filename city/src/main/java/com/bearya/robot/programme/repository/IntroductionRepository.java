package com.bearya.robot.programme.repository;

import com.bearya.robot.programme.entity.IntroductionEntity;

public class IntroductionRepository {

    private static IntroductionRepository repository = null;

    public static IntroductionRepository getInstance() {
        if (repository == null) {
            repository = new IntroductionRepository();
        }
        return repository;
    }

    private IntroductionRepository() {

    }

    public final IntroductionEntity[] fetch(String itemName) {
        switch (itemName) {
            case "revolution":
                return revolutionEntities;
            case "protection":
                return protectionEntities;
            default:
                return new IntroductionEntity[]{};
        }
    }

    private final IntroductionEntity[] revolutionEntities = new IntroductionEntity[]{
            new IntroductionEntity(IntroductionEntity.PHOTO, "theme/introduce/revolution.webp", "theme/dub/revolution.mp3")
    };

    private final IntroductionEntity[] protectionEntities = new IntroductionEntity[] {
            new IntroductionEntity(IntroductionEntity.AUDIO, "theme/introduce/protection.webp", "theme/dub/protection.mp3")
    };

}