
pipeline {
    agent any

    // 1. כאן שואלים את המשתמש שאלות (Parameters)
    parameters {
        // בחירה מתוך רשימה (סביבת פריסה)
        choice(
            name: 'TARGET_ENV', 
            choices: ['dev', 'staging', 'production'], 
            description: 'בחר לאיזו סביבה לפרוס'
        )

        // תיבת סימון של כן/לא (האם להריץ בדיקות)
        booleanParam(
            name: 'RUN_TESTS', 
            defaultValue: true, 
            description: 'האם להריץ בדיקות מקדימות?'
        )
    }

    // 2. משתנים קבועים שהגדרנו מראש (Environment Variables)
    environment {
        APP_NAME = 'my-awesome-app'
        APP_VERSION = '1.0.0'
    }

    stages {
        // שלב ראשון: בדיקה והצגה של מה שנבחר
        stage('Print Config') {
            steps {
                echo "Deploying application: ${env.APP_NAME} (Version: ${env.APP_VERSION})"
                echo "Selected environment: ${params.TARGET_ENV}"
            }
        }

        // שלב שני: מותנה בפרמטר שסומן
        stage('Run Tests') {
            steps {
                script {
                    if (params.RUN_TESTS) {
                        echo "Running automated tests on ${params.TARGET_ENV}..."
                    } else {
                        echo "Skipping tests as requested!"
                    }
                }
            }
        }

        // שלב שלישי: הפריסה עצמה
        stage('Deploy') {
            steps {
                echo "Deploying ${env.APP_NAME} to: ${params.TARGET_ENV}"
                sh "echo Deploy completed successfully to ${params.TARGET_ENV}"
            }
        }
    }
}
