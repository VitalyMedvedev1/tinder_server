package ru.liga.homework.util.form;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.liga.homework.exception.BusinessLogicException;
import ru.liga.homework.exception.CustomIOFileException;
import ru.liga.homework.util.FileWorker;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.List;

/**
 * Накладывание текста на картинку(фон). Берем 2 поля с клиента: header и Description, берем прямоугольник в качесве основы с размерами картинки.
 * По циклу берем шрифт 64, делем на ликии по размеру прямоуголдьника и вычисляем высоту, если больше чем размер прямоугольника, уменьшаем шрифт.
 * Если текст подошел по размеру, вприсываем на картинку.
 **/
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UsersFormGenerator implements UsersForm {

    private final FileWorker fileWorker;
    private static final String BACKGROUND_FILE_NAME = "background.jpg";
    private static final String FONT_NAME = "Old Standard TT";
    private static final int FONT_SIZE = 64;
    private static final int FONT_HEADER_SIZE = 10;
    private static final int LEFT_INDENT = 15;

    @Override
    public String createUserForm(Long userTgId, String header, String description) {
        log.debug("Start create form for userName {}", userTgId);
        BufferedImage image;
        try (InputStream inputStream = new ClassPathResource(BACKGROUND_FILE_NAME).getInputStream()) {
            image = ImageIO.read(inputStream);
        } catch (IOException e) {
            throw new CustomIOFileException("Error when create form for userName " + userTgId + "\n" + e.getMessage());
        }
        header = header + ",";
        Rectangle rectangle = new Rectangle(image.getWidth(), image.getHeight());
        List<TextLayout> testLines = new ArrayList<>();
        List<TextLayout> headerLines = new ArrayList<>();
        int fontSize = FONT_SIZE;
        float headerHeight;
        float descHeight;
        Graphics2D g2 = (Graphics2D) image.getGraphics();
        g2.setColor(Color.BLACK);
        do {
            testLines.clear();
            headerLines.clear();

            Font headerFont = new Font(FONT_NAME, Font.BOLD, fontSize + FONT_HEADER_SIZE);
            Font descFont = new Font(FONT_NAME, Font.PLAIN, fontSize);

            headerHeight = calcTextHeightAndTextLine(header, headerLines, headerFont, g2, rectangle);
            if (description.equals("")) {
                descHeight = 0;
            } else {
                descHeight = calcTextHeightAndTextLine(description, testLines, descFont, g2, rectangle);
            }
            fontSize -= 2;
        }
        while (descHeight > rectangle.getHeight() - headerHeight);

        float y = (rectangle.height - headerHeight - descHeight) / 2;

        testLines.addAll(0, headerLines);

        for (TextLayout line : testLines) {
            line.draw(g2, (float) LEFT_INDENT, y + line.getAscent());
            y += line.getAscent() + line.getDescent() + line.getLeading();
        }
        String fileName = fileWorker.saveUserFormOnDiscAndReturnPath(image, userTgId);
        image.flush();
        return fileName;
    }

    private float calcTextHeightAndTextLine(String text, List<TextLayout> textLines, Font font, Graphics2D graphics2D, Rectangle rectangle) {
        log.debug("Start calc Text Height FORM");
        AttributedString attributedString = new AttributedString(text);
        attributedString.addAttribute(TextAttribute.FONT, font);
        AttributedCharacterIterator iterator = attributedString.getIterator();
        LineBreakMeasurer measurer = new LineBreakMeasurer(iterator, graphics2D.getFontRenderContext());
        float textHeight;
        while (measurer.getPosition() < text.length()) {
            textLines.add(measurer.nextLayout(rectangle.width - LEFT_INDENT));
        }
        textHeight = textLines.stream().mapToInt(t -> (int) (t.getAscent() + t.getDescent() + t.getLeading())).sum();
        log.debug("Text Height: {}", textHeight);
        return textHeight;
    }
}
