//
// EvhRentalAdminGetItemListRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhGetItemListCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalAdminGetItemListRestResponse
//
@interface EvhRentalAdminGetItemListRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhGetItemListCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
