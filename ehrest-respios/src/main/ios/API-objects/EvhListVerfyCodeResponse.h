//
// EvhListVerfyCodeResponse.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhUserIdentifierDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListVerfyCodeResponse
//
@interface EvhListVerfyCodeResponse
    : NSObject<EvhJsonSerializable>


// item type EvhUserIdentifierDTO*
@property(nonatomic, strong) NSMutableArray* values;

@property(nonatomic, copy) NSNumber* nextPageAnchor;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

