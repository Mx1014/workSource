//
// EvhAclinkListDoorAccessQRKeyRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhListDoorAccessQRKeyResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAclinkListDoorAccessQRKeyRestResponse
//
@interface EvhAclinkListDoorAccessQRKeyRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListDoorAccessQRKeyResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
