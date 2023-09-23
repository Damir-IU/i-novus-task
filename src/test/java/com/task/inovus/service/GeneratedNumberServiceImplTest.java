package com.task.inovus.service;

import com.task.inovus.domain.RepeatException;
import com.task.inovus.domain.model.GeneratedNumber;
import com.task.inovus.repository.GeneratedNumberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Random;

import static com.task.inovus.constants.ApiConstants.LOCATION;
import static com.task.inovus.constants.ApiConstants.MAX_NUMBER_VALUE;
import static com.task.inovus.constants.ApiConstants.NUMBER_CHARACTERS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyChar;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GeneratedNumberServiceImplTest {
    @InjectMocks
    private GeneratedNumberServiceImpl generatedNumberService;
    @Mock
    private GeneratedNumberRepository generatedNumberRepository;
    @Mock
    private Random random;

    @Nested
    @DisplayName("test of 'generateRandomNumber()' method")
    class GenerateRandomNumber {
        @Test
        public void testPositiveFirstTry() {
            int randomNumber = 1;

            when(random.nextInt(anyInt())).thenReturn(randomNumber);
            when(generatedNumberRepository.existsByParams(anyChar(), anyInt(), anyChar(), anyChar()))
                    .thenReturn(false);

            String result = generatedNumberService.generateRandomNumber();

            verify(generatedNumberRepository, times(1)).save(any());
            char randomChar = NUMBER_CHARACTERS.get(randomNumber);
            String expected = String.format("%s%s%s%s %s", randomChar,
                    String.format("%03d", randomNumber),
                    randomChar,
                    randomChar,
                    LOCATION);
            assertEquals(expected, result);
        }

        @Test
        public void testPositiveSecondTry() {
            int randomNumber = 0;

            for (int i = 0; i < 2; i++) {
                randomNumber++;
                when(random.nextInt(anyInt())).thenReturn(randomNumber);
                when(random.nextInt(anyInt())).thenReturn(randomNumber);
                when(random.nextInt(anyInt())).thenReturn(randomNumber);
                when(random.nextInt(anyInt())).thenReturn(randomNumber);
            }
            when(generatedNumberRepository.existsByParams(anyChar(), anyInt(), anyChar(), anyChar()))
                    .thenReturn(true);
            when(generatedNumberRepository.existsByParams(anyChar(), anyInt(), anyChar(), anyChar()))
                    .thenReturn(false);

            String result = generatedNumberService.generateRandomNumber();

            verify(generatedNumberRepository, times(1)).save(any());

            char randomChar = NUMBER_CHARACTERS.get(randomNumber);
            String expected = String.format("%s%s%s%s %s", randomChar,
                    String.format("%03d", randomNumber),
                    randomChar,
                    randomChar,
                    LOCATION);
            assertEquals(expected, result);
        }

        @Test
        public void testNegative() {
            int randomNumber = 1;

            when(random.nextInt(anyInt())).thenReturn(randomNumber);
            when(generatedNumberRepository.existsByParams(anyChar(), anyInt(), anyChar(), anyChar()))
                    .thenReturn(true);

            assertThrows(RepeatException.class, () -> generatedNumberService.generateRandomNumber());

            verify(generatedNumberRepository, times(0)).save(any());
        }
    }

    @Nested
    @DisplayName("test of 'generateNextNumber()' method")
    class GenerateNextNumber {
        @Test
        public void testPreviousNumberNotExists() {
            int randomNumber = 1;

            when(generatedNumberRepository.findLast()).thenReturn(Optional.empty());
            when(random.nextInt(anyInt())).thenReturn(randomNumber);
            when(generatedNumberRepository.existsByParams(anyChar(), anyInt(), anyChar(), anyChar()))
                    .thenReturn(false);

            String result = generatedNumberService.generateNextNumber();

            verify(generatedNumberRepository, times(1)).save(any());
            char randomChar = NUMBER_CHARACTERS.get(randomNumber);
            String expected = String.format("%s%s%s%s %s", randomChar,
                    String.format("%03d", randomNumber),
                    randomChar,
                    randomChar,
                    LOCATION);
            assertEquals(expected, result);
        }

        @Test
        public void testPositive() {
            int randomNumber = 1;

            when(generatedNumberRepository.findLast())
                    .thenReturn(Optional.of(GeneratedNumber.builder()
                            .firstChar(NUMBER_CHARACTERS.get(randomNumber))
                            .number(randomNumber)
                            .secondChar(NUMBER_CHARACTERS.get(randomNumber))
                            .thirdChar(NUMBER_CHARACTERS.get(randomNumber))
                            .location(LOCATION)
                            .build()));

            String result = generatedNumberService.generateNextNumber();

            verify(generatedNumberRepository, times(1)).save(any());
            char randomChar = NUMBER_CHARACTERS.get(randomNumber);
            String expected = String.format("%s%s%s%s %s", randomChar,
                    String.format("%03d", randomNumber + 1),
                    randomChar,
                    randomChar,
                    LOCATION);
            assertEquals(expected, result);
        }

        @Test
        public void testPositiveLastChars() {
            int randomCharNumber = NUMBER_CHARACTERS.size() - 1;
            int randomNumber = MAX_NUMBER_VALUE - 1;

            when(generatedNumberRepository.findLast())
                    .thenReturn(Optional.of(GeneratedNumber.builder()
                            .firstChar(NUMBER_CHARACTERS.get(randomCharNumber))
                            .number(randomNumber)
                            .secondChar(NUMBER_CHARACTERS.get(randomCharNumber))
                            .thirdChar(NUMBER_CHARACTERS.get(randomCharNumber))
                            .location(LOCATION)
                            .build()));

            String result = generatedNumberService.generateNextNumber();

            verify(generatedNumberRepository, times(1)).save(any());
            char randomChar = NUMBER_CHARACTERS.get(0);
            String expected = String.format("%s%s%s%s %s", randomChar,
                    String.format("%03d", 0),
                    randomChar,
                    randomChar,
                    LOCATION);
            assertEquals(expected, result);
        }
    }
}