
# Online Кинотеатр "Filmorate"
---
1. Добавляет пользователей и фильмы
2. Тестирует граничные условия
3. Есть Rest-контроллеры
4. Логирует
   
5. Приложение написано на Java. 
Пример кода:
```java
@PostMapping
public User create(@Valid @RequestBody User user) {
        validateUser(user);
        log.info("Пользователь {} добавлен в список.", user);
        users.put(user.getId(), user);
        return user;
        }
```
