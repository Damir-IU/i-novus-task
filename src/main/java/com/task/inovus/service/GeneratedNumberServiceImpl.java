package com.task.inovus.service;

import com.task.inovus.domain.RepeatException;
import com.task.inovus.domain.model.GeneratedNumber;
import com.task.inovus.repository.GeneratedNumberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import static com.task.inovus.constants.ApiConstants.LOCATION;
import static com.task.inovus.constants.ApiConstants.MAX_NUMBER_VALUE;
import static com.task.inovus.constants.ApiConstants.NUMBER_CHARACTERS;
import static com.task.inovus.constants.ApiConstants.REPEAT_COUNT;

@Service
@Transactional
@RequiredArgsConstructor
public class GeneratedNumberServiceImpl implements GeneratedNumberService {
    private final GeneratedNumberRepository generatedNumberRepository;
    private final Random random;

    @Override
    public String generateRandomNumber() {
        GeneratedNumber generatedNumber = getRandomGeneratedNumber(NUMBER_CHARACTERS);

        generatedNumberRepository.save(generatedNumber);
        return generatedNumber.getFullNumber();
    }

    private GeneratedNumber getRandomGeneratedNumber(List<Character> characters) {
        char firstChar = getRandomCharFromArray(random, characters);
        int number = random.nextInt(MAX_NUMBER_VALUE);
        char secondChar = getRandomCharFromArray(random, characters);
        char thirdChar = getRandomCharFromArray(random, characters);

        int count = 0;
        while (generatedNumberRepository.existsByParams(firstChar, number, secondChar, thirdChar)) {
            firstChar = getRandomCharFromArray(random, characters);
            number = random.nextInt(MAX_NUMBER_VALUE);
            secondChar = getRandomCharFromArray(random, characters);
            thirdChar = getRandomCharFromArray(random, characters);

            count++;
            if (count == REPEAT_COUNT) {
                throw new RepeatException("Limit of repeat count");
            }
        }

        return GeneratedNumber.builder()
                .firstChar(firstChar)
                .number(number)
                .secondChar(secondChar)
                .thirdChar(thirdChar)
                .location(LOCATION)
                .build();
    }

    private char getRandomCharFromArray(Random random, List<Character> characters) {
        int randomPos = random.nextInt(characters.size());
        return characters.get(randomPos);
    }

    @Override
    public String generateNextNumber() {
        Optional<GeneratedNumber> optional = generatedNumberRepository.findLast();
        if (optional.isEmpty()) {
            return generateRandomNumber();
        }

        GeneratedNumber nextGeneratedNumber = getNextGeneratedNumber(optional.get(), NUMBER_CHARACTERS);

        generatedNumberRepository.save(nextGeneratedNumber);
        return nextGeneratedNumber.getFullNumber();
    }

    private GeneratedNumber getNextGeneratedNumber(GeneratedNumber oldGeneratedNumber, List<Character> characters) {
        int sizeSymbols = characters.size();

        int number = oldGeneratedNumber.getNumber() + 1;
        int firstIndex = characters.indexOf(oldGeneratedNumber.getFirstChar());
        int secondIndex = characters.indexOf(oldGeneratedNumber.getSecondChar());
        int thirdIndex = characters.indexOf(oldGeneratedNumber.getThirdChar());

        if (number == MAX_NUMBER_VALUE) {
            number = 0;

            thirdIndex++;
            if (thirdIndex == sizeSymbols) {
                thirdIndex = 0;

                secondIndex++;
                if (secondIndex == sizeSymbols) {
                    secondIndex = 0;

                    firstIndex++;
                    if (firstIndex == sizeSymbols) {
                        firstIndex = 0;
                    }
                }
            }
        }

        return GeneratedNumber.builder()
                .firstChar(characters.get(firstIndex))
                .number(number)
                .secondChar(characters.get(secondIndex))
                .thirdChar(characters.get(thirdIndex))
                .location(LOCATION)
                .build();
    }
}