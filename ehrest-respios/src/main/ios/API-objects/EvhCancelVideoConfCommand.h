//
// EvhCancelVideoConfCommand.h
// generated at 2016-03-25 09:26:39 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCancelVideoConfCommand
//
@interface EvhCancelVideoConfCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* confId;

@property(nonatomic, copy) NSString* sourceAccountName;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

