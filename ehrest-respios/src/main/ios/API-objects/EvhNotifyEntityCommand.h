//
// EvhNotifyEntityCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhNotifyEntityCommand
//
@interface EvhNotifyEntityCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* token;

@property(nonatomic, copy) NSString* sign;

@property(nonatomic, copy) NSString* msg;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

