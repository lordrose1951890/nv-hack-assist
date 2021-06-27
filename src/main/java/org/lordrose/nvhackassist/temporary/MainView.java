package org.lordrose.nvhackassist.temporary;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextAreaVariant;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import org.lordrose.nvhackassist.nv.MasterMind;
import org.lordrose.nvhackassist.nv.Option;

import java.util.Arrays;

import static com.vaadin.flow.theme.lumo.Lumo.DARK;

@Route
@PWA(name = "Vaadin Application",
        shortName = "app",
        description = "description",
        enableInstallPrompt = false)
@Theme(value = Lumo.class, variant = DARK)
public class MainView extends VerticalLayout {

    MasterMind masterMind;
    VerticalLayout masterMindPanel = new VerticalLayout();

    public MainView() {
        setAlignItems(Alignment.CENTER);

        TextArea wordsText = new TextArea("Words", "Type here...");
        wordsText.addThemeVariants(TextAreaVariant.LUMO_ALIGN_CENTER);
        wordsText.setHeight(200, Unit.PERCENTAGE);

        Button start = new Button("Start", e -> initMasterMind(wordsText.getValue()));
        start.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button reset = new Button("Reset", e -> {
            masterMindPanel.removeAll();
            wordsText.setValue("");
        });
        reset.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        HorizontalLayout buttonLine = new HorizontalLayout(start, reset);

        add(wordsText, buttonLine, masterMindPanel);
    }

    private void initMasterMind(String input) {
        String trimmed = input.trim();
        if (trimmed.contains(" ")) {
            masterMind = MasterMind.from(trimmed.split(" "));
        } else if (trimmed.contains("\n") || trimmed.contains("\r")) {
            masterMind = MasterMind.from(trimmed.lines().toArray(String[]::new));
        }
        initMasterMindView();
    }

    private void initMasterMindView() {
        masterMindPanel.removeAll();
        if (masterMind != null) {
            if (masterMind.getOptions().size() > 1) {
                for (Option option : masterMind.getOptions()) {
                    HorizontalLayout line = new HorizontalLayout();
                    line.setAlignItems(Alignment.CENTER);
                    line.setMargin(true);
                    line.add(new Button("Dud", e -> dud(option.getIndex())));
                    Arrays.stream(option.getCorrectCharOptions())
                            .forEach(optionVal -> {
                                Button optionButton = new Button(String.valueOf(optionVal),
                                        e -> evaluate(option.getIndex(), optionVal));
                                optionButton.addThemeVariants(ButtonVariant.MATERIAL_OUTLINED);
                                line.add(optionButton);
                            });
                    line.add(new Label(option.getWord()));
                    masterMindPanel.add(line);
                }
            } else {
                HorizontalLayout line = new HorizontalLayout();
                line.setAlignItems(Alignment.CENTER);
                line.setMargin(true);
                Label message = new Label("The answer is: ");
                Label answer = new Label(masterMind.getOptions().get(0).getWord());
                answer.getStyle().set("color", "red");
                line.add(message, answer);
                masterMindPanel.add(line);
            }
        }
    }

    private void evaluate(int index, int numOfCorrectChar) {
        masterMind.evaluate(index, numOfCorrectChar);
        initMasterMindView();
    }

    private void dud(int index) {
        masterMind.dud(index);
        initMasterMindView();
    }
}
