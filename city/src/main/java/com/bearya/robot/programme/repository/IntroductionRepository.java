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
                return introductionContents;
            default:
                return new IntroductionEntity[]{};
        }
    }

    private final IntroductionEntity[] introductionContents = new IntroductionEntity[]{
            new IntroductionEntity(IntroductionEntity.PHOTO, "theme/introduce/revolution.webp", "theme/dub/revolution.mp3"),
            new IntroductionEntity(IntroductionEntity.AUDIO, "theme/introduce/protection.webp", "theme/dub/protection.mp3"),
    };

}