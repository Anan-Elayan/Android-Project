import pymysql
from flask import Flask,request, render_template, jsonify
from config import Flask, mysql, app


@app.route('/getStudent')
def students():
    try:
        conn = mysql.connect()
        cursor = conn.cursor(pymysql.cursors.DictCursor)
        cursor.execute("SELECT * FROM student")
        bookRows = cursor.fetchall()
        respone = jsonify(bookRows)  # convert data as a response
        respone.status_code = 200
        return respone
    except Exception as e:
        print(e)
    finally:
        cursor.close()
        conn.close()


@app.route('/getAllCourses')
def getAllCourses():
    try:
        conn = mysql.connect()
        cursor = conn.cursor(pymysql.cursors.DictCursor)
        cursor.execute("SELECT * FROM course")
        bookRows = cursor.fetchall()
        respone = jsonify(bookRows)  # convert data as a response
        respone.status_code = 200
        return respone
    except Exception as e:
        print(e)
    finally:
        cursor.close()
        conn.close()


@app.route('/getCourses/<string:studentID>')
def getCourses(studentID):
    try:
        conn = mysql.connect()
        cursor = conn.cursor(pymysql.cursors.DictCursor)
        cursor.execute("SELECT courseID FROM enrolment where studentID = %s",(studentID))
        bookRows = cursor.fetchall()
        respone = jsonify(bookRows)  # convert data as a response
        respone.status_code = 200
        return respone
    except Exception as e:
        print(e)
    finally:
        cursor.close()
        conn.close()


@app.route('/getCoursesById/<string:courseID>', methods=['GET'])
def getCoursesById(courseID):
    try:
        conn = mysql.connect()
        cursor = conn.cursor(pymysql.cursors.DictCursor)
        cursor.execute("SELECT * FROM course where courseID = %s", (courseID))
        bookRows = cursor.fetchall()
        respone = jsonify(bookRows)  # convert data to a response
        respone.status_code = 200
        return respone
    except Exception as e:
        print(e)
        # Handle the exception and return an appropriate response
        return jsonify({'error': 'An error occurred while processing your request'}), 500
    finally:
        cursor.close()
        conn.close()


@app.route('/getTasks/<string:courseID>')
def getTasks(courseID):
    try:
        conn = mysql.connect()
        cursor = conn.cursor(pymysql.cursors.DictCursor)
        cursor.execute("SELECT taskID FROM coursetask where courseID = %s",(courseID))
        bookRows = cursor.fetchall()
        respone = jsonify(bookRows)  # convert data as a response
        respone.status_code = 200
        return respone
    except Exception as e:
        print(e)
    finally:
        cursor.close()
        conn.close()


@app.route('/getTasksById/<string:studentID>/<string:courseID>', methods=['GET'])
def getTasksById( studentID, courseID):
    try:
        conn = mysql.connect()
        cursor = conn.cursor(pymysql.cursors.DictCursor)
        cursor.execute("SELECT * FROM task where   studentID = %s and courseID = %s ", (studentID,courseID))
        bookRows = cursor.fetchall()
        respone = jsonify(bookRows)  # convert data to a response
        respone.status_code = 200
        return respone
    except Exception as e:
        print(e)
        # Handle the exception and return an appropriate response
        return jsonify({'error': 'An error occurred while processing your request'}), 500
    finally:
        cursor.close()
        conn.close()


@app.route('/insertIntoCourseTasks/<string:courseID>/<int:taskID>', methods=['POST'])
def insert_into_course_tasks(courseID, taskID):
    try:
        _json = request.json
        _courseid = courseID
        _taskid = taskID

        if _courseid and _taskid and request.method == 'POST':
            with mysql.connect() as conn, conn.cursor(pymysql.cursors.DictCursor) as cursor:
                sqlQuery = "INSERT INTO coursetask VALUES (%s, %s)"
                bindData = (_courseid, _taskid)
                cursor.execute(sqlQuery, bindData)
                conn.commit()
                response = jsonify(result='Task added successfully!')
                response.status_code = 200
                return response
        else:
            return showMessage()
    except Exception as e:
        print(f"Exception: {e}")
        response = jsonify(error=f"An error occurred: {str(e)}")
        response.status_code = 500
        return response


@app.route('/addNewCourse/<string:courseID>/<string:courseDr>/<string:courseStartTime>/<string:courseEndTime>/<string:date>', methods=['POST'])
def addNewCourse(courseID,courseDr,courseStartTime,courseEndTime,date):
    try:
        _json = request.json
        _id = courseID
        _drName = courseDr
        _startTime = courseStartTime
        _endTime = courseEndTime
        _date = date

        if _id and _drName and _startTime and _endTime and _date and request.method == 'POST':
            conn = mysql.connect()
            cursor = conn.cursor(pymysql.cursors.DictCursor)
            sqlQuery = "INSERT INTO courses VALUES( %s, %s, %s , %s  ,%s)"
            bindData = (_id, _drName, _startTime, _endTime , _date)
            cursor.execute(sqlQuery, bindData)
            conn.commit()
            respone = jsonify(result='تم اضافة المساق بنجاح 😊')
            respone.status_code = 200
            return respone

        else:
            return showMessage()
    except Exception as e:
        print(f"Exception: {e}")
        raise
    finally:
        cursor.close()
        conn.close()


@app.route('/registerationCourse/<string:studentID>/<string:courseID>/<string:courseDr>', methods=['POST'])
def registerationCourse(studentID,courseID,courseDr): #enrolment
    try:
        _json = request.json
        _courseId = courseID
        _coursedStudentID = studentID
        _courseDr = courseDr

        if _courseId and _coursedStudentID and _courseDr and request.method == 'POST':
            conn = mysql.connect()
            cursor = conn.cursor(pymysql.cursors.DictCursor)

            sqlQuery = "INSERT INTO enrolment VALUES (%s, %s,%s)"
            bindData = ( _coursedStudentID, _courseId , _courseDr)
            cursor.execute(sqlQuery, bindData)
            conn.commit()
            respone = jsonify(result='تم التسجيل المساق بنجاح 😊')
            respone.status_code = 200
            return respone

        else:
            return showMessage()
    except Exception as e:
        print(f"Exception: {e}")
        raise
    finally:
        cursor.close()
        conn.close()


@app.route('/register', methods=['POST'])
def register():
    try:
        _json = request.json
        _id = _json['studentID']
        _name = _json['studentName']
        _email = _json['studentEmail']
        _password = _json['studentPassword']
        _image = _json['studentImage']

        if _id and _name and _email and _password and _image and request.method == 'POST':
            conn = mysql.connect()
            cursor = conn.cursor(pymysql.cursors.DictCursor)

            sqlQuery = "INSERT INTO student VALUES( %s, %s, %s , %s  ,%s)"
            bindData = (_id, _name, _email, _password , _image)
            cursor.execute(sqlQuery, bindData)
            conn.commit()
            respone = jsonify(result='register successfully!')
            respone.status_code = 200
            return jsonify({"result": "اهلا وسهلا فيك يا محترم 🥰"})
            return respone

        else:
            return showMessage()
    except Exception as e:
        print(f"Exception: {e}")
        raise
    finally:
        cursor.close()
        conn.close()


@app.route('/getStudent/<int:studentID>')
def getStudent(studentID):
    try:
        conn = mysql.connect()
        cursor = conn.cursor(pymysql.cursors.DictCursor)
        cursor.execute("SELECT * FROM student WHERE studentID = %s", (studentID))
        student_row = cursor.fetchone()

        if student_row:
            response = jsonify(student_row)
            response.status_code = 200
        else:
            response = jsonify({'error': 'Student not found'})
            response.status_code = 404

        return response
    except Exception as e:
        print(f"Error: {e}")
        response = jsonify({'error': 'Internal Server Error'})
        response.status_code = 500
        return response
    finally:
        if cursor:
            cursor.close()
        if conn:
            conn.close()
            print("Connection closed")


@app.route('/deleteCourse/<string:studentID>/<string:courseID>', methods=['DELETE'])
def deleteCourse(studentID, courseID):
    try:
        conn = mysql.connect()
        cursor = conn.cursor(pymysql.cursors.DictCursor)

        cursor.execute("DELETE FROM enrolment WHERE studentID = %s AND courseID = %s", (studentID, courseID))
        affected_rows = cursor.rowcount

        if affected_rows > 0:
            conn.commit()
            response = jsonify({'message': 'الله ريّحك من هالمساق 😁'})
            response.status_code = 200
        else:
            response = jsonify({'error': 'Course not found or already deleted'})
            response.status_code = 404

        return response
    except Exception as e:
        print(f"Error: {e}")
        response = jsonify({'error': 'Internal Server Error'})
        response.status_code = 500
        return response
    finally:
        if cursor:
            cursor.close()
        if conn:
            conn.close()
            print("Connection closed")


@app.route('/deleteTask/<string:taskID>', methods=['DELETE'])
def deleteTask(taskID):
    try:
        conn = mysql.connect()
        cursor = conn.cursor(pymysql.cursors.DictCursor)

        cursor.execute("DELETE FROM task WHERE taskID = %s" , (taskID))
        affected_rows = cursor.rowcount

        if affected_rows > 0:
            conn.commit()
            response = jsonify({'message': 'الله ريّحك من هالمهمة 😁'})
            response.status_code = 200
        else:
            response = jsonify({'error': 'Course not found or already deleted'})
            response.status_code = 404

        return response
    except Exception as e:
        print(f"Error: {e}")
        response = jsonify({'error': 'Internal Server Error'})
        response.status_code = 500
        return response
    finally:
        if cursor:
            cursor.close()
        if conn:
            conn.close()
            print("Connection closed")


@app.route('/updateStudent/<int:studentID>/<string:studentEmail>/<string:studentPassword>',methods=['PUT'])
def updateStudent(studentID, studentEmail, studentPassword):
    try:
        conn = mysql.connect()
        cursor = conn.cursor(pymysql.cursors.DictCursor)
        # Update the column names in the SET clause
        cursor.execute("UPDATE student SET studentEmail = %s, studentPassword = %s  WHERE studentID = %s ",
                    (studentEmail, studentPassword,studentID))

        # Commit the changes
        conn.commit()

        # Check if any rows were affected by the update
        if cursor.rowcount > 0:
            response = jsonify({'message': 'تم تحديث بياناتك بنجاح 🥳'})
            response.status_code = 200
        else:
            response = jsonify({'error': 'Student not found'})
            response.status_code = 404

        return response
    except Exception as e:
        print(f"Error: {e}")
        response = jsonify({'error': 'Internal Server Error'})
        response.status_code = 500
        return response
    finally:
        if cursor:
            cursor.close()
        if conn:
            conn.close()
            print("Connection closed")


@app.route('/addTask/<string:courseID>/<string:taskTitle>/<string:taskDescription>/<string:taskTime>/<string:taskDate>/<string:studentID>',methods=["POST"])
def addTask(courseID, taskTitle, taskDescription, taskTime, taskDate,studentID):
    try:
        conn = mysql.connect()

        cursor = conn.cursor(pymysql.cursors.DictCursor)
        cursor.execute(
            "INSERT INTO task (courseID, taskTitle, taskDescription, taskTime, taskDate,studentID ) VALUES (%s, %s, %s, %s, %s,%s)",
            (courseID, taskTitle, taskDescription, taskTime, taskDate,studentID))

        # Commit the changes to the database
        conn.commit()

        response = jsonify({'result': 'تم اضافة المهمة بنجاح 👍'})
        response.status_code = 200

        return response
    except Exception as e:
        print(f"Error: {e}")
        response = jsonify({'error': 'Internal Server Error'})
        response.status_code = 500
        return response
    finally:
        if cursor:
            cursor.close()
        if conn:
            conn.close()
            print("Connection closed")


@app.route('/updateTasks/<string:courseID>/<string:taskTitle>/<string:taskDescription>/<string:taskTime>/<string:taskDate>/<string:studentID>/<string:taskID>',methods=['PUT'])
def updateTask(courseID,taskTitle,taskDescription,taskTime,taskDate,studentID,taskID):
    try:
        conn = mysql.connect()
        cursor = conn.cursor(pymysql.cursors.DictCursor)
        # Update the column names in the SET clause
        cursor.execute("UPDATE task SET taskTitle = %s, taskDescription = %s , taskTime=%s , taskDate=%s  WHERE studentID = %s and taskID=%s and  courseID=%s",
                    (taskTitle, taskDescription,taskTime, taskDate, studentID,taskID,courseID))

        # Commit the changes
        conn.commit()

        # Check if any rows were affected by the update
        if cursor.rowcount > 0:
            response = jsonify({'message': 'تم تحديث بيانات المهمة بنجاح 🥳'})
            response.status_code = 200
        else:
            response = jsonify({'error': 'Student not found'})
            response.status_code = 404

        return response
    except Exception as e:
        print(f"Error: {e}")
        response = jsonify({'error': 'Internal Server Error'})
        response.status_code = 500
        return response
    finally:
        if cursor:
            cursor.close()
        if conn:
            conn.close()
            print("Connection closed")

@app.route('/getTask/<string:taskID>', methods=['GET'])
def getTaskById(taskID):
    try:
        conn = mysql.connect()
        cursor = conn.cursor(pymysql.cursors.DictCursor)
        cursor.execute("SELECT * FROM task where taskID = %s",(taskID))
        bookRows = cursor.fetchall()
        respone = jsonify(bookRows)  # convert data as a response
        respone.status_code = 200
        return respone
    except Exception as e:
        print(e)
    finally:
        cursor.close()
        conn.close()


@app.route('/getLastTask', methods=['GET'])
def getLastTask():
    try:
        conn = mysql.connect()
        cursor = conn.cursor(pymysql.cursors.DictCursor)
        cursor.execute("SELECT taskID FROM task ORDER BY taskID DESC LIMIT 1;")
        bookRows = cursor.fetchall()
        respone = jsonify(bookRows)  # convert data as a response
        respone.status_code = 200
        return respone
    except Exception as e:
        print(e)
    finally:
        cursor.close()
        conn.close()



@app.errorhandler(404)
def showMessage(error=None):
    message = {
        'status': 404,
        'message': 'Record not found: ' + request.url,
    }
    respone = jsonify(message)
    respone.status_code = 404
    return respone


if __name__ == '__main__':
    app.debug = True
    app.run()
