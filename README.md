# Antree Order API (1/3)
Sebuah API yang akan mengatur antrean pemesanan. API ini menggunakan Kotlin [Ktor](https://ktor.io/) 
untuk pembuatan backend.

## Library
1. [Kotlinx-Datetime](https://github.com/Kotlin/kotlinx-datetime) for date & time
2. [Koin](https://insert-koin.io/docs/reference/koin-ktor/ktor/) for Dependency Injection for Ktor
3. [Exposed](https://github.com/JetBrains/Exposed) for Database SQL Helper
4. Used Serialization, Auth, and any more Ktor libraries

## Database
- [PostgreSQL](https://www.postgresql.org/)

## Min Requirement
- Java 11 or higher (to use Ktor Koin)

## Documentation
 - [Wiki](../../wiki)

## Screenshoots
![Index](/screenshoots/index.jpg)

## Next Update
- Get data by bearer token
- Using Role
- Add API Key
- Endpoint based on role
