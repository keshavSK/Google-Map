import 'package:flutter/material.dart';
import 'package:flutter_puneagro_admin/presenters/MainPresenter.dart';
import 'package:flutter_puneagro_admin/presenters/ProductPurchaseListPresenter.dart';
import 'package:flutter_puneagro_admin/responses/ProductPurchaseListResponse.dart';
import 'package:flutter_puneagro_admin/utils/Constants.dart';
import 'package:flutter_puneagro_admin/utils/Utilities.dart';
import 'package:flutter_puneagro_admin/views/CommonView.dart';
import 'package:intl/intl.dart';

import 'ProductBuyerNavigationDrawer.dart';

class ProductBuyerHomeScreen extends StatefulWidget {
  @override
  _ProductBuyerHomeScreenState createState() => _ProductBuyerHomeScreenState();
}

class _ProductBuyerHomeScreenState extends State<ProductBuyerHomeScreen>
    implements ProductPurchaseListView {
  bool isLoading = false;
  var productList = new List<PurchaseProducts>();
  DateTime selectedDate;
  String selectedPurchaseDate;
  var myFormat = DateFormat('yyyy-MM-dd');
  ProductPurchaseListPresenter _productPurchaseListPresenter;

  @override
  void initState() {
    selectedPurchaseDate = myFormat.format(DateTime.now());
    _productPurchaseListPresenter = new ProductPurchaseListPresenter(this);
    if (!Utilities.isEmpty(selectedPurchaseDate)) {
      _productPurchaseListPresenter.getPurchasedProducts(
          MainPresenter.getInstance().userId, selectedPurchaseDate);
    }
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: backgroundColor,
      appBar: AppBar(
        backgroundColor: primaryColor,
        title: Text('Purchased Products'),
      ),
      body: Stack(
        children: [
          _productPurchasedBody(context),
          isLoading ? getCircleIndicator() : Container()
        ],
      ),
      drawer: ProductBuyerNavigationDrawer(),
    );
  }

  _productPurchasedBody(BuildContext context) {
    return Container(
      margin: EdgeInsets.all(15.0),
      child: Column(
        mainAxisAlignment: MainAxisAlignment.start,
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text(
            'Select Purchased Date',
            style: T