//
//  UIButton.swift
//  DragGame
//
//  Created by anranxinghai on 2022/1/9.
//  Copyright © 2022 anranxinghai. All rights reserved.
//

import Foundation
import Moya
import SwiftUI
import SwiftyJSON
class UIButtonViewController:UIViewController{
    private var uiButton:UIButton!
    
    var changeName:ChangeLocationNameDelegate?
    override func viewDidLoad() {
        super.viewDidLoad()
        view.backgroundColor = .white
        navigationItem.title = "UIButton"
        uiButton = UIButton.init()
        uiButton.setTitle("我是UIButton", for: .normal)
        uiButton.addTarget(self, action: #selector(buttonClick), for: .touchUpInside)
        
        view.addSubview(uiButton)
        uiButton.translatesAutoresizingMaskIntoConstraints = false
        uiButton.centerXAnchor.constraint(equalTo: view.centerXAnchor,constant: 0).isActive = true
        uiButton.setTitleColor(.red, for: .normal)
        uiButton.topAnchor.constraint(equalTo: view.topAnchor,constant:  100).isActive = true
        uiButton.widthAnchor.constraint(equalToConstant: 100).isActive = true
        uiButton.heightAnchor.constraint(equalToConstant: 50).isActive = true
        
    }
    
    @objc func buttonClick(sender:UIButton){
        sender.isSelected.toggle()
//        sender.isSelected  = !sender.isSelected
        if(sender.isSelected) {
            uiButton.setTitle("我被选中了", for: .normal)
            changeName?.changeName()
        }
        else {
            uiButton.setTitle("没有选中我", for: .normal)
        }
        
        let provider = MoyaProvider<GitHub>()
        provider.request(.weather){ result in
            var message = "Could't access API"
            if case let .success(response) = result{
                let jsonString = try? response.mapString()
                message = jsonString ?? message
                
                let json = try? jsonString!.data(using: .utf8)
                let tData = JSON(json)
                print("\(tData["weatherinfo"]["city"]) 的 温度是\(tData["weatherinfo"]["temp1"])~\(tData["weatherinfo"]["temp2"])")
            }
            if case let .failure(error) = result{
                print("\(error.errorDescription)")
            }
//            self.showAlert("Zen",message:message)
            
        }
            
    }
    
}
public enum GitHub{
    case zen
    case useProfile(String)
    case useRepositories(String)
    case weather
}

extension GitHub:TargetType{
    public var baseURL: URL {
        return URL(string: "http://www.weather.com.cn")!
    }
    
    public var path: String {
        switch self {
        case .zen: return "/zen"
        case .useProfile(let name):
            return "/users/\(name)"
        case .useRepositories(let name):
            return "/users/\(name)/repos"
        case .weather: return "/data/cityinfo/101010100.html"
        }
    }
    
    public var method: Moya.Method {
        return .get
    }
    
    public var headers: [String : String]? {
        return nil
    }
    
    
    public var task: Task {
        switch self{
        case .useRepositories:
            return .requestParameters(parameters: ["sort":"push"], encoding: URLEncoding.default)
        default: return .requestPlain
        }
    }
    public var validationType:ValidationType{
            switch self{
            case .zen:
                return .successCodes
            default:
                return .none
            }
        }
    
    
}
