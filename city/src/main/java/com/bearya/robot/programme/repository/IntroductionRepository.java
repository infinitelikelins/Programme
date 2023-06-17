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
            case "":
                return introductionContents;
            default: return new IntroductionEntity[]{};
        }
    }

    private final IntroductionEntity[] introductionContents = new IntroductionEntity[]{
            new IntroductionEntity(IntroductionEntity.AUDIO, "", ""),
            new IntroductionEntity(IntroductionEntity.VIDEO, "", ""),
            new IntroductionEntity(IntroductionEntity.PICTURE, "", ""),
    };

}
