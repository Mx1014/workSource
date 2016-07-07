//
// EvhSearchCardUsersResponse.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhCardUserDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSearchCardUsersResponse
//
@interface EvhSearchCardUsersResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* nextPageAnchor;

// item type EvhCardUserDTO*
@property(nonatomic, strong) NSMutableArray* requests;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

