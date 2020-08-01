import dbUtil
import pymysql
from dbUtil import pool

def create_conn():
    conn = pool.connection()
    cursor = conn.cursor(pymysql.cursors.DictCursor)
    return conn, cursor


def close_conn(conn, cursor):
    conn.close()
    cursor.close()


def select_one(sql, args):
    conn, cur = create_conn()
    cur.execute(sql, args)
    result = cur.fetchone()
    close_conn(conn, cur)
    return result


def select_all(sql, args):
    conn, cur = create_conn()
    cur.execute(sql, args)
    result = cur.fetchall()
    close_conn(conn, cur)
    return result


def insert_one(sql, args):
    conn, cur = create_conn()
    cur.execute(sql, args)
    result = cur.lastrowid
    conn.commit()
    close_conn(conn, cur)
    return result

def insert_many(sql, args):
    conn, cur = create_conn()
    result = cur.executemany(sql, args)
    conn.commit()
    close_conn(conn, cur)
    return result

def delete_one(sql,args):
    conn,cur = create_conn()
    result = cur.execute(sql,args)
    conn.commit()
    close_conn(conn,cur)
    return result

def update_one(sql,args):
    conn,cur = create_conn()
    result = cur.execute(sql,args)
    conn.commit()
    close_conn(conn,cur)
    return result