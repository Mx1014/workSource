//
// EvhAddUserPointCommand.h
// generated at 2016-03-28 15:56:07 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAddUserPointCommand
//
@interface EvhAddUserPointCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* operatorUid;

@property(nonatomic, copy) NSString* pointType;

@property(nonatomic, copy) NSNumber* point;

@property(nonatomic, copy) NSNumber* uid;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

