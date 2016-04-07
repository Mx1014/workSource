//
// EvhAppCreateCommand.h
// generated at 2016-04-07 17:33:48 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAppCreateCommand
//
@interface EvhAppCreateCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* appKey;

@property(nonatomic, copy) NSString* creatorUid;

@property(nonatomic, copy) NSString* name;

@property(nonatomic, copy) NSString* secretKey;

@property(nonatomic, copy) NSString* description_;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

