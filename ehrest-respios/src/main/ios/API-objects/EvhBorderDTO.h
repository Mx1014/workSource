//
// EvhBorderDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhBorderDTO
//
@interface EvhBorderDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSString* privateAddress;

@property(nonatomic, copy) NSNumber* privatePort;

@property(nonatomic, copy) NSString* publicAddress;

@property(nonatomic, copy) NSNumber* publicPort;

@property(nonatomic, copy) NSNumber* status;

@property(nonatomic, copy) NSString* configTag;

@property(nonatomic, copy) NSString* description_;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

