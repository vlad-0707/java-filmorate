
# Online Кинотеатр "Filmorate"
---
1. Добавляет пользователей и фильмы
2. Тестирует граничные условия
3. Есть Rest-контроллеры
4. Логирует
5. Добавляет\удаляет пользователей в друзья\из друзей
6. Показывает самые залайканые фильмы
7. Приложение написано на Java.
Пример кода:
```
@PostMapping
public User create(@Valid @RequestBody User user) {
        validateUser(user);
        log.info("Пользователь {} добавлен в список.", user);
        users.put(user.getId(), user);
        return user;
        }
```
![Диграмма Filmorate](../../Desktop/диаграмма.png)
Запрос на все фильмы из таблицы
```
SELECT *
from films
```

Запрос на email и имя пользователя
```
SELECT u.email, 
u.name
FROM users AS u
```

Запрос на название жанра
```
SELECT g1.name AS genre
FROM film_genre AS fg
INNER JOIN film AS f ON f.film_id=fg.film_id
INNER JOIN genre AS g1 ON fg.genre_id=g1.genre_id
WHERE g1.name = 'Комедия'
```

Запрос на друзей пользователя
```
SELECT u.user_id,
       u.email,
       u.login,
       u.name,
       u.date_birthday
FROM friendship AS f
INNER JOIN users AS u ON u.user_id = f.friend_id
WHERE f.user_id = userId AND f.status = true
UNION
SELECT u.user_id,
       u.email,
       u.login,
       u.name,
       u.date_birthday
FROM friendship AS f
INNER JOIN users AS u ON u.user_id = f.user_id
WHERE f.friend_id = userId AND f.status = true;
```

