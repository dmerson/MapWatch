import sqlite3
conn = sqlite3.connect('monitor.db')

c = conn.cursor()

c.execute('''CREATE TABLE IF NOT EXISTS monitor
             (mapProvider text, time int)''')

c.execute('''CREATE TABLE IF NOT EXISTS monitorerror
             (mapProvider text, time int, level text)''')

c.execute('''CREATE TABLE IF NOT EXISTS monitorchange
             (oldFetchJob int, newFetchJob int, oldPath text, newPath text, oldHash text, newHash text, country text, mapProvider text)''')

conn.commit()

conn.close()