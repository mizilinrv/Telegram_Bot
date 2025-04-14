document.addEventListener("DOMContentLoaded", function () {

    // Глобальный массив вариантов ответа
    let globalOptions = [];

    // Функция для добавления возможных ответов (один список для всех вопросов)
    window.addOptions = function () {
        let numOptions = document.getElementById("numOptions").value;
        let container = document.getElementById("optionsContainer");
        container.innerHTML = "";
        globalOptions = []; // Очищаем массив перед добавлением новых вариантов

        for (let i = 1; i <= numOptions; i++) {
            let optionDiv = document.createElement("div");
            let optionText = `Ответ ${i}`;

            optionDiv.innerHTML = `
                <label>${optionText}:</label>
                <input type="text" class="form-control" required id="optionText${i}"/>
                <label>Баллы:</label>
                <input type="number" class="form-control" required id="optionPoints${i}"/>
            `;
            container.appendChild(optionDiv);
        }
    };

    // Функция для добавления вопросов (только текст, без вариантов ответа)
    window.addQuestions = function () {
        let numQuestions = document.getElementById("numQuestions").value;
        let container = document.getElementById("questionsContainer");
        container.innerHTML = "";

        for (let i = 1; i <= numQuestions; i++) {
            let questionDiv = document.createElement("div");
            questionDiv.classList.add("question-block");
            questionDiv.innerHTML = `
                <h3>Вопрос ${i}</h3>
                <label>Текст вопроса:</label>
                <input type="text" name="questions[${i - 1}].text" class="form-control" required/>
            `;
            container.appendChild(questionDiv);
        }
    };
});

window.addResults = function () {
    let numResults = document.getElementById("numResults").value;
    let container = document.getElementById("resultsContainer");
    container.innerHTML = "";

    for (let i = 1; i <= numResults; i++) {
        let resultDiv = document.createElement("div");
        resultDiv.innerHTML = `
            <h3>Результат ${i}</h3>
            <label>Минимальный балл:</label>
            <input type="number" name="results[${i - 1}].minPoints" class="form-control" required/>
            <label>Максимальный балл:</label>
            <input type="number" name="results[${i - 1}].maxPoints" class="form-control" required/>
            <label>Текст результата:</label>
            <textarea name="results[${i - 1}].text" class="form-control" required></textarea>
        `;
        container.appendChild(resultDiv);
    }
};