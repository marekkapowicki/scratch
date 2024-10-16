package pl.marekk.scratch.domain.board;

import pl.marekk.scratch.common.RandomGenerator;

class BoardCellFactory {
    private final Probabilities probabilities;


    private BoardCellFactory(Probabilities probabilities) {
        this.probabilities = probabilities;
    }

    static BoardCellFactory boardCellFactory(Probabilities probabilities) {
        return new BoardCellFactory(probabilities);
    }

    private static String randomSymbol(Probabilities.SymbolsInCellProbabilities possibleSymbolsInCell) {
        final int randomNumberInRange = RandomGenerator.randomNumberInRange(possibleSymbolsInCell.numberToDrawRange());
        final Probabilities.CellProbability symbolOnPositionBasedOnGeneratedValue = possibleSymbolsInCell.findSymbolOnPositionBasedOnGeneratedValue(randomNumberInRange);
        return symbolOnPositionBasedOnGeneratedValue.symbol();
    }

    String generateBonusSymbolForCell() {
        final Probabilities.SymbolsInCellProbabilities bonusSymbol = probabilities.findBonusSymbolProbabilities();
        return randomSymbol(bonusSymbol);
    }

    String generateStandardSymbolForCell(int row, int column) {
        Probabilities.SymbolsInCellProbabilities possibleSymbolsInCell = probabilities.findStandardSymbolsProbabilitiesForCell(row, column);
        return randomSymbol(possibleSymbolsInCell);
    }


}


