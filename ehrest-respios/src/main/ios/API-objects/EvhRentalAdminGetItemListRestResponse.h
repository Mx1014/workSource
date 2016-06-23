//
// EvhRentalAdminGetItemListRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhgetItemListCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalAdminGetItemListRestResponse
//
@interface EvhRentalAdminGetItemListRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhgetItemListCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
