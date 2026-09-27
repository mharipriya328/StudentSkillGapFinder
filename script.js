async function findSkillGap() {

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


    // Send student information to Java backend

    const skills = selectedSkills.join(",");

    const url =
        "http://localhost:8080/skillgap?career="
        + encodeURIComponent(career)
        + "&skills="
        + encodeURIComponent(skills);


    try {

        const response = await fetch(url);

        const data = await response.json();


        if (data.error) {

            alert(data.error);
            return;

        }


        // Display career

        const careerNames = {

            data_analyst: "Data Analyst",
            data_scientist: "Data Scientist",
            web_developer: "Web Developer",
            cloud_engineer: "Cloud Engineer"

        };


        document.getElementById("careerResult").innerText =
            "Target Career: " + careerNames[career];


        // Display missing skills

        const missingList =
            document.getElementById("missingSkills");

        missingList.innerHTML = "";


        if (data.missingSkills.length === 0) {

            const li = document.createElement("li");

            li.innerText =
                "🎉 You have all the required skills!";

            li.style.color = "green";

            missingList.appendChild(li);

        } else {

            data.missingSkills.forEach(function (skill) {

                const li = document.createElement("li");

                li.innerText = skill.toUpperCase();

                missingList.appendChild(li);

            });

        }


        // Create learning roadmap

        const roadmap =
            document.getElementById("roadmap");

        roadmap.innerHTML = "";


        data.missingSkills.forEach(function (skill) {

            const li = document.createElement("li");

            li.innerText =
                "Learn " + skill.toUpperCase();

            roadmap.appendChild(li);

        });


        // Show result

        document.getElementById("result").style.display =
            "block";

    }

    catch (error) {

        alert(
            "Cannot connect to Java backend. Make sure the Java server is running."
        );

        console.error(error);

    }

}