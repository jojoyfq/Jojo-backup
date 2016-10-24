/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.filter;

import java.io.IOException;
import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author liyanmeng
 */
@WebFilter(filterName = "SecurityFilter", urlPatterns = {"/DepositManagement/*", "/FixedDepositManagement/*", "/MessageManagement/*", "/StaffMessageManagement/*", "/TransferManagement/*", "/CustomerManagement/*", "/dashboard.xhtml"})
//@WebFilter(filterName = "SecurityFilter", urlPatterns = {"*.xhtml"})
public class SecurityFilter implements Filter {

    private FilterConfig filterConfig = null;

    public SecurityFilter() {
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        final String REDIRECT = "?faces-redirect=true";
        try {
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            HttpSession httpSession = httpServletRequest.getSession(true);
            String requestServletPath = httpServletRequest.getServletPath();

            if ((!requestServletPath.equals("/LogInHome.xhtml")) && (!requestServletPath.equals("/index.xhtml"))
                    && (!requestServletPath.equals("/CustomerManagement/DisplayAccountTypeChoice.xhtml")) && (!requestServletPath.equals("/CustomerManagement/createSavingAccount.xhtml"))
                    && (!requestServletPath.equals("/CustomerManagement/ResetPasswordVerifyCustomerDetails.xhtml")) && (!requestServletPath.equals("/CustomerManagement/SubmitTwoFA.xhtml"))
                    && (!requestServletPath.equals("/CustomerManagement/ResetPassword.xhtml")) && (!requestServletPath.equals("/CustomerManagement/createFixedDepositAccount.xhtml"))
                    && (!requestServletPath.equals("/CustomerManagement/configureFixedDepositAccount.xhtml")) && (!requestServletPath.equals("/CustomerManagement/CustomerAccountActivation.xhtml"))
                    && (!requestServletPath.equals("/CustomerManagement/ResetInitialPassword.xhtml")) && (!requestServletPath.equals("/customerSuccessPageWOLogIn.xhtml"))&& (!requestServletPath.equals("/CustomerManagement/uploadFile.xhtml"))) {
                System.out.println("Check Security Filter");

                Boolean isLogin = (httpSession.getAttribute("isLogin") != null) ? ((Boolean) httpSession.getAttribute("isLogin")) : (false);

                if (!isLogin) {
                    HttpServletResponse res = (HttpServletResponse) response;
                    String contextPath = httpServletRequest.getContextPath();
                    res.sendRedirect(contextPath + "/LogInHome.xhtml" + REDIRECT);
                }
            }
//            if(!requestServletPath.equals("/LogInHome.xhtml")){
                chain.doFilter(request, response);
//            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void destroy() {
    }

}
