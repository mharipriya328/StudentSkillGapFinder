function findSkillGap() {

    const career = document.getElementById("career").value;

    const selectedSkills = [];

    const checkboxes = document.querySelectorAll(
        '.skills input[type="checkbox"]'
    );

    checkboxes.forEach(function (checkbox) {

        if (checkbox.checked) {
            selectedSkills.push(checkbox.value);
        }

    });


    // Career names

    const careerNames = {

        data_analyst: "Data Analyst",
        data_scientist: "Data Scientist",
        web_developer: "Web Developer",
        cloud_engineer: "Cloud Engineer"

    };


    // Required skills for each career

    const requiredSkills = {

        data_analyst: [
            "python",
            "sql",
            "excel",
            "power bi",
            "statistics"
        ],

        data_scientist: [
            "python",
            "sql",
            "statistics",
            "machine learning",
            "pandas"
        ],

        web_developer: [
            "html",
            "css",
            "javascript",
            "react",
            "git"
        ],

        cloud_engineer: [
            "linux",
            "networking",
            "aws",
            "docker",
            "python"
        ]

    };


    // Check whether career exists

    if (!requiredSkills[career]) {

        alert("Please select a career.");

        return;

    }


    // Convert selected skills to lowercase

    const studentSkills = selectedSkills.map(function (skill) {

        return skill.toLowerCase();

    });


    // Find missing skills

    const missingSkills = requiredSkills[career].filter(
        function (skill) {

            return !studentSkills.includes(skill.toLowerCase());

        }
    );


    // Display career

    document.getElementById("careerResult").innerText =
        "Target Career: " + careerNames[career];


    // Display missing skills

    const missingList =
        document.getElementById("missingSkills");

    missingList.innerHTML = "";


    if (missingSkills.length === 0) {

        const li = document.createElement("li");

        li.innerText =
            "🎉 You have all the required skills!";

        li.style.color = "green";

        missingList.appendChild(li);

    } else {

        missingSkills.forEach(function (skill) {

            const li = document.createElement("li");

            li.innerText = skill.toUpperCase();

            missingList.appendChild(li);

        });

    }


    // Create learning roadmap

    const roadmap =
        document.getElementById("roadmap");

    roadmap.innerHTML = "";


    missingSkills.forEach(function (skill) {

        const li = document.createElement("li");

        li.innerText =
            "Learn " + skill.toUpperCase();

        roadmap.appendChild(li);

    });


    // Show result

    document.getElementById("result").style.display =
        "block";

}
