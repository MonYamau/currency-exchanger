package com.project.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.dao.CurrencyDao;
import com.project.dao.ExchangeRateDao;
import com.project.database.DatabaseManager;
import com.project.database.SQLiteDatabaseManager;
import com.project.factory.ExchangeRateProviderFactory;
import com.project.provider.ExchangeRateProvider;
import com.project.service.CurrencyService;
import com.project.service.ExchangeRateService;
import com.project.service.ExchangerService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppContextListener implements ServletContextListener {
    private DatabaseManager databaseManager;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        this.databaseManager = new SQLiteDatabaseManager();
        databaseManager.init();
        ObjectMapper objectMapper = new ObjectMapper();
        CurrencyDao currencyDao = new CurrencyDao(databaseManager.getDataSource());
        ExchangeRateDao exchangeRateDao = new ExchangeRateDao(databaseManager.getDataSource());
        ExchangeRateProvider exchangeRateProvider = new ExchangeRateProviderFactory().create();
        CurrencyService currencyService = new CurrencyService(currencyDao);
        ExchangeRateService exchangeRateService = new ExchangeRateService(exchangeRateDao);
        ExchangerService exchangerService = new ExchangerService(currencyDao, exchangeRateDao, exchangeRateProvider);
        ServletContext context = sce.getServletContext();
        context.setAttribute("CurrencyService", currencyService);
        context.setAttribute("ExchangeRateService", exchangeRateService);
        context.setAttribute("ExchangerService", exchangerService);
        context.setAttribute("ObjectMapper", objectMapper);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (databaseManager != null) {
            databaseManager.close();
        }
    }
}
