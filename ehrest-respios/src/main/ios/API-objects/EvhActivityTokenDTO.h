//
// EvhActivityTokenDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhActivityTokenDTO
//
@interface EvhActivityTokenDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* postId;

@property(nonatomic, copy) NSNumber* forumId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

