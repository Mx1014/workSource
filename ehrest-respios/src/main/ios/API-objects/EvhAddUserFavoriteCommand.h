//
// EvhAddUserFavoriteCommand.h
// generated at 2016-04-08 20:09:23 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAddUserFavoriteCommand
//
@interface EvhAddUserFavoriteCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* targetType;

@property(nonatomic, copy) NSNumber* targetId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

