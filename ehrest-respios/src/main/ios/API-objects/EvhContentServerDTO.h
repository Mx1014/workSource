//
// EvhContentServerDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhContentServerDTO
//
@interface EvhContentServerDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSString* name;

@property(nonatomic, copy) NSString* description_;

@property(nonatomic, copy) NSString* privateAddress;

@property(nonatomic, copy) NSNumber* privatePort;

@property(nonatomic, copy) NSString* publicAddress;

@property(nonatomic, copy) NSNumber* publicPort;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

