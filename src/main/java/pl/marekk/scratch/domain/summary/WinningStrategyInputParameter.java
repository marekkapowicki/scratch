package pl.marekk.scratch.domain.summary;

import java.util.List;

public record WinningStrategyInputParameter(String name, Double rewardMultiplier, String when, Integer count,
                                            String group,
                                            List<List<String>> coveredAreas) {

}
