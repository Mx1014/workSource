//
// EvhFetchCardCommand.h
// generated at 2016-03-25 09:26:41 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhFetchCardCommand
//
@interface EvhFetchCardCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* applierPhone;

@property(nonatomic, copy) NSNumber* communityId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

