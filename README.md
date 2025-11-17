# FoodCampus - Online Food Ordering Platform

FoodCampus is a Spring Boot-based online food ordering system that allows customers to browse food items, place orders, and receive personalized food recommendations based on their health profile using AI (Gemini API).

## Features

- **User Management**: Registration, login, and profile management
- **Product Catalog**: Browse and search food items by category
- **Shopping Cart**: Add/remove items and manage orders
- **Order Management**: Place orders and track order history
- **Admin Dashboard**: Manage products, categories, users, and orders
- **Health-Based Recommendations**: Get personalized food recommendations using Gemini AI based on your health profile (BMI, age, health conditions)
- **Email Notifications**: Order confirmations and password reset emails
- **Secure Authentication**: Spring Security for user authentication and authorization

## Technology Stack

- **Backend**: Spring Boot 3.2.3
- **Database**: Microsoft SQL Server (Production), H2 (Testing)
- **ORM**: Hibernate/JPA
- **Security**: Spring Security
- **Template Engine**: Thymeleaf
- **AI Integration**: Google Gemini API
- **Build Tool**: Maven
- **Java Version**: 17

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- Microsoft SQL Server (for production)
- Gmail account with App Password (for email functionality)
- Google Gemini API Key (for AI recommendations)

## Installation & Setup

### 1. Clone the repository

```bash
git clone https://github.com/Nafis1e18/FoodCampus.git
cd FoodCampus
```

### 2. Configure Environment Variables

Copy the `.env.example` file to create your own environment configuration:

```bash
cp .env.example .env
```

Or create a `.env` file in the root directory with the following variables:

```properties
# Database Configuration
DB_URL=jdbc:sqlserver://localhost:1433;databaseName=foodcampusDB;encrypt=false
DB_USERNAME=sa
DB_PASSWORD=your_secure_password

# Email Configuration (use App Password for Gmail)
MAIL_HOST=smtp.gmail.com
MAIL_USERNAME=your_email@gmail.com
MAIL_PASSWORD=your_app_password
MAIL_PORT=587

# Gemini API Configuration
GEMINI_API_KEY=your_gemini_api_key_here
```

**Important**: 
- For Gmail, you need to create an [App Password](https://support.google.com/accounts/answer/185833)
- Get your Gemini API key from [Google AI Studio](https://makersuite.google.com/app/apikey)

### 3. Database Setup

Create a new database in SQL Server:

```sql
CREATE DATABASE foodcampusDB;
```

The application will automatically create the necessary tables on first run due to `spring.jpa.hibernate.ddl-auto=update`.

### 4. Build the Application

```bash
./mvnw clean install
```

### 5. Run the Application

```bash
./mvnw spring-boot:run
```

The application will start on `http://localhost:8080`

### 6. Access the Application

- **Home Page**: http://localhost:8080/
- **Admin Login**: http://localhost:8080/signin
  - Default admin credentials are created automatically on first run
  - Check the console logs for the default admin email and password

## Running Tests

The project includes unit and integration tests that use H2 in-memory database:

```bash
./mvnw test
```

## Project Structure

```
FoodCampus/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/foodcampus/
│   │   │       ├── config/          # Security and application configuration
│   │   │       ├── controller/      # REST and web controllers
│   │   │       ├── dto/             # Data Transfer Objects
│   │   │       ├── model/           # JPA entities
│   │   │       ├── repository/      # Data access layer
│   │   │       ├── service/         # Business logic
│   │   │       └── util/            # Utility classes
│   │   └── resources/
│   │       ├── static/              # CSS, JS, images
│   │       ├── templates/           # Thymeleaf templates
│   │       └── application.properties
│   └── test/                        # Test files
├── .env.example                     # Environment variables template
├── pom.xml                          # Maven dependencies
└── README.md
```

## Security

⚠️ **Important Security Notes**:

1. **Never commit sensitive credentials** to version control
2. All sensitive configuration should be in environment variables or `.env` file
3. The `.env` file is excluded from Git via `.gitignore`
4. Use `application.properties.template` as a reference for required configuration
5. Always use strong passwords for database and admin accounts
6. For Gmail, use App Passwords instead of your actual password

## API Integration

### Gemini AI Recommendations

The application uses Google's Gemini API to provide personalized food recommendations based on:
- User's BMI (calculated from height and weight)
- Age
- Health conditions/diseases
- Available menu items

To use this feature:
1. Obtain an API key from [Google AI Studio](https://makersuite.google.com/app/apikey)
2. Add it to your `.env` file as `GEMINI_API_KEY`

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Support

For issues, questions, or contributions, please open an issue on the GitHub repository.

## Authors

- Nafis Ahmed - [Nafis1e18](https://github.com/Nafis1e18)

## Acknowledgments

- Spring Boot team for the excellent framework
- Google for the Gemini AI API
- All contributors who help improve this project
