//
// EvhAddUserFavoriteCommand.h
// generated at 2016-03-25 17:08:10 
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

