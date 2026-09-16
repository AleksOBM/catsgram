# Catsgram

![Static Badge](https://img.shields.io/badge/Java-21-green)
![Static Badge](https://img.shields.io/badge/Spring_Boot-3.2.2-green)
![Static Badge](https://img.shields.io/badge/REST-red)
![Static Badge](https://img.shields.io/badge/MVC-green)
![Static Badge](https://img.shields.io/badge/Lombok-red)
![Static Badge](https://img.shields.io/badge/JdbcTemplate-8A2BE2)
![Static Badge](https://img.shields.io/badge/PostgreSQL-16.1-blue)
![Static Badge](https://img.shields.io/badge/NIO-538681)
![Static Badge](https://img.shields.io/badge/Maven-orange)
![Static Badge](https://img.shields.io/badge/docker_compose-blue)

![cats-image.jpeg](.img/cats-image.jpeg)

<img alt="catsgram.png" src=".img/catsgram.png" width="500"/>  

**Инстаграм для питомцев** - учебный проект по REST MVC.  

Основные возможности:
- Добавлять/удалять пользователей
- Оставлять посты от имени пользователя
- Добавлять фото и видео к постам.
- Просматривать посты и их файлы.

### Http API
```text
API
├── ImageController
│   ├── 🌐/posts/{postId}/images
│   │   ├── GET /posts/{postId}/images
│   │   └── POST /posts/{postId}/images
│   └── 🌐/images
│       └── GET /images/{imageId}
├── MovieController
│   ├── 🌐/posts/{postId}/movies
│   │   ├── GET /posts/{postId}/movies
│   │   └── POST /posts/{postId}/movies
│   └── 🌐/movies
│       └── GET /movies/{movieId}
├── PostController
│   └── 🌐/posts
│       ├── GET /posts
│       ├── POST /posts
│       └── PUT /posts
│       ├── GET /posts/{id}
└── UserController
    └── 🌐/users
        ├── GET /users
        ├── POST /users
        ├── GET /users/{userId}
        └── PUT /users/{userId}
```

### Database map
```mermaid
%%{init: { 'theme': 'dark', 'themeVariables': {
    'fontFamily': 'Arial',
    'fontSize': '10px'
} }}%%
erDiagram
    POSTS {
        bigint id PK
        bigint author_id FK
        text description
        timestamp post_date
    }

    IMAGE_STORAGE {
        bigint id PK
        varchar original_name
        varchar file_path
        bigint post_id FK
    }

    USERS {
        bigint id PK
        varchar username
        varchar email
        varchar password
        timestamp registration_date
    }

    POSTS ||--o{ IMAGE_STORAGE : "contains"
    USERS ||--o{ POSTS : "writes"
```