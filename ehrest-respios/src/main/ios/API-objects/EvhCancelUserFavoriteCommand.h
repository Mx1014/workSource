//
// EvhCancelUserFavoriteCommand.h
// generated at 2016-03-31 15:43:23 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCancelUserFavoriteCommand
//
@interface EvhCancelUserFavoriteCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* targetType;

@property(nonatomic, copy) NSNumber* targetId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

