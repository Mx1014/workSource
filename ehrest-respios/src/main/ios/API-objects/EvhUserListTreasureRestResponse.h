//
// EvhUserListTreasureRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhListTreasureResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUserListTreasureRestResponse
//
@interface EvhUserListTreasureRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListTreasureResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
