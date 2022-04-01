package ru.liga.homework.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import ru.liga.homework.api.UsersFormService;
import ru.liga.homework.exception.BusinessLogicException;
import ru.liga.homework.model.User.UserView;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class DefaultUsersFormService implements UsersFormService {

    private static final String BACKGROUND_FILE_NAME = "background.jpg";
    private static final String FONT_NAME = "Old Standard TT";
    private static final int FONT_SIZE = 98;
    private static final int FONT_HEADER_SIZE = 10;
    private static final int LEFT_INDENT = 15;
    private static final String FILE_EXT = "png";
    private static final String FILE_PATH_NAME = "/forms/form.";
    private static final String USER_DIR = System.getProperty("user.dir");

    @Override
    public void createUserForm(UserView userView) {
        log.debug("Start create form for user {}", userView.getId());
        try (InputStream inputStream = new ClassPathResource(BACKGROUND_FILE_NAME).getInputStream()) {
            BufferedImage image = ImageIO.read(inputStream);
            String header = userView.getHeader() + ",";
            String description = userView.getDescription();
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
                descHeight = calcTextHeightAndTextLine(description, testLines, descFont, g2, rectangle);
                fontSize -= 2;
            }
            while (descHeight > rectangle.getHeight() - headerHeight);

            float y = (rectangle.height - headerHeight - descHeight) / 2;

            testLines.addAll(0, headerLines);

            for (TextLayout line : testLines
            ) {
                Rectangle2D bounds = line.getBounds();
                line.draw(g2, (float) LEFT_INDENT, y + line.getAscent());
                y += line.getAscent() + line.getDescent() + line.getLeading();
            }

            saveUserFormOnDiscAndReturnAbsPath(image, userView.getId());
            image.flush();
        } catch (IOException e) {
            log.error("Error when create form for user {} \n {}", userView.getId(), e.getMessage());
            throw new BusinessLogicException("Error when create form for user " + userView.getId() + "\n" + e.getMessage());
        }
    }

    @Override
    public String saveUserFormOnDiscAndReturnAbsPath(BufferedImage image, Long userId) {
        try {
            log.debug("Save form on disc for user {}", userId);
            File file = new File(USER_DIR + FILE_PATH_NAME + FILE_EXT);
            ImageIO.write(image, FILE_EXT, new File(USER_DIR + FILE_PATH_NAME + userId + FILE_EXT));
            return file.getAbsolutePath();
        } catch (IOException e) {
            log.error("Error when save form for user {} \n {}", userId, e.getMessage());
            throw new BusinessLogicException("Error when save form for user " + userId + "\n" + e.getMessage());
        }
    }

    private float calcTextHeightAndTextLine(String text, List<TextLayout> textLines, Font font, Graphics2D graphics2D, Rectangle rectangle) {
        log.debug("Start calc Text Height FORM");
        AttributedString attributedString = new AttributedString(text);
        attributedString.addAttribute(TextAttribute.FONT, font);
        AttributedCharacterIterator iterator = attributedString.getIterator();
        LineBreakMeasurer measurer = new LineBreakMeasurer(iterator, graphics2D.getFontRenderContext());
        float textHeight = 0;
        while (measurer.getPosition() < text.length()) {
            textLines.add(measurer.nextLayout(rectangle.width - LEFT_INDENT));
        }
        for (TextLayout line : textLines
        ) {
            textHeight += line.getAscent() + line.getDescent() + line.getLeading();
        }
        log.debug("Text Height: {}", textHeight);
        return textHeight;
    }
}
