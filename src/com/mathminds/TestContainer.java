package com.mathminds;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class TestContainer {


    public ArrayList<Question> questions = new ArrayList<>();
    public String title;
    public int questionCount;
    public boolean shouldQuestionsBeUniqueType;
    public ArrayList<TemplateQuestion> templates;


    public TestContainer(String title, int questionCount, boolean shouldQuestionsBeUniqueType, ArrayList<TemplateQuestion> templates) {
        this.title = title;
        this.questionCount = questionCount;
        this.shouldQuestionsBeUniqueType = shouldQuestionsBeUniqueType;
        this.templates = templates;
    }


    public void populateQuestions() {
        System.out.println("Populating test with questions");

        Random random = new Random();
        for (int qNum = 1; qNum <= questionCount; qNum++) {

            int id;
            String type;
            String templateText;
            ArrayList<String> fieldNames;
            HashMap<String, String> fields;
            Method solver;

            TemplateQuestion potentialTemplateQuestion = templates.get(random.nextInt(0, templates.size()));

            //don't add the question if its type is already present.
            if (shouldQuestionsBeUniqueType) {
                boolean isTemplatePresent = false;
                for (Question q : questions) {
                    if (potentialTemplateQuestion.type.equals(q.type)) {
                        isTemplatePresent = true;
                        break;
                    }
                }
                if (isTemplatePresent) {
                    qNum--;
                    continue;
                }
            }

            id = potentialTemplateQuestion.id;
            type = potentialTemplateQuestion.type;
            templateText = potentialTemplateQuestion.asText;
            fieldNames = potentialTemplateQuestion.fields;
            fields = new HashMap<>();

            for (String key : fieldNames) {
                fields.put(key, "");
            }

            TemplateSolvers accessSolvers = new TemplateSolvers();

            solver = accessSolvers.retrieveMethod(potentialTemplateQuestion.keyMethod);

            Question question = new Question(id, type, templateText, fieldNames, fields, solver);


            questions.add(question);
        }
    }


    public void randomizeQuestionFields() {
        for (Question q : questions) {
            q.randomizeFields();
        }
    }
}
